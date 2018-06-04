package com.evolutiongaming.cassam

import com.datastax.driver.core._
import de.kaufhof.pillar
import de.kaufhof.pillar.SimpleStrategy
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FreeSpec, Matchers}

class CassandraMigratorSpec extends FreeSpec with Matchers with MockFactory{
  "CassandraMigrator" - {
    "creates a keyspace" in {
      val testSettings = MigratorSettings(
        keyspace = "test_keyspace",
        migrationScriptPath = "/test-migrations",
        migrationTable = "applied_migrations"
      )

      val session = stub[Session]

      val strategy = SimpleStrategy(replicationFactor = 2)
      new CassandraMigrator(testSettings).initKeyspace(session, strategy)

      verifyQueryOnce(keyspaceQuery(testSettings.keyspace, strategy), session)
    }

    "executes migrations" in {
      val testSettings = MigratorSettings(
        keyspace = "test_keyspace",
        migrationScriptPath = "/test-migrations",
        migrationTable = "applied_migrations"
      )

      val session = stub[Session]
      (session.execute(_:Statement)).when(*).returns(emptyResultSet())
      (session.execute(_:String)).when(*).returns(stub[ResultSet])

      new CassandraMigrator(testSettings).migrate(session)

      verifyQueryOnce(s"USE ${testSettings.keyspace}", session)
      verifyQueryOnce(migrationTableQuery(testSettings), session)
      //check that two queries from migration files are executed
      verifyQueryOnce(
        "CREATE TABLE test_table1 (id text, data text, updated_at timestamp, PRIMARY KEY (id))",
        session)
      verifyQueryOnce(
        "CREATE TABLE test_table2 (id text, data text, updated_at timestamp, PRIMARY KEY (id))",
        session)
    }
  }

  private def verifyQueryOnce(
    query: String,
    session: Session
  ): Unit = {
    (session.execute(_:String)).verify(query).once()
  }

//  private def verifyQueryContains(
//    value: String,
//    session: Session
//  ): Unit = {
//    (session.execute(_:String)).verify(where { string: String => string.contains(value) }).once()
//  }

  private def verifyQueryOnce(
    queryChecker: String => Boolean,
    session: Session
  ): Unit = {
    (session.execute(_:String)).verify(where(queryChecker)).once()
  }

  private def emptyResultSet(results: Seq[Row] = Seq.empty): ResultSet = {
    import scala.collection.JavaConverters._
    val rows = results.asJava
    val resultSet = stub[ResultSet]
    (resultSet.all _).when().returning(rows)
    resultSet
  }

  private def keyspaceQuery(keyspace: String, replicationStrategy: pillar.ReplicationStrategy): String = {
    s"CREATE KEYSPACE IF NOT EXISTS $keyspace " +
    s"WITH replication = ${replicationStrategy.cql}"
  }

  private def migrationTableQuery(settings: MigratorSettings): String => Boolean = { input =>
    input.contains(s"CREATE TABLE IF NOT EXISTS ${settings.keyspace}.${settings.migrationTable}")
  }
}
