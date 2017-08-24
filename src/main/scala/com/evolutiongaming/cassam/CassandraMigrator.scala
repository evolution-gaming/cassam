package com.evolutiongaming.cassam

import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets

import com.datastax.driver.core.Session
import de.kaufhof.pillar.{Migrator, Parser, Registry, ReplicationStrategy}

/**
  * Loads and executes migrations for cassandra
  * @param settings settings for migrations
  */
class CassandraMigrator(settings: MigratorSettings) {
  /**
    * Creates a keyspace(if configured) and executes migration scripts of a DB schema using an external session
    * @param session datastax session
    */
  def migrate(session: Session): Unit = {
    val scripts = FileLoader.entriesAt(settings.migrationScriptPath)
    val registry = parseScripts(scripts)
    val migrator = Migrator(registry, LoggerReporter, settings.migrationTable)
    migrator.migrate(session)
  }

  def migrate(connConfig: CassandraConfig): Unit = {
    val client = new SimpleSyncClient(connConfig)
    client.withSession(migrate)
  }

  /**
    * Creates a keyspace
    * @param session datastax session
    */
  def initKeyspace(session: Session, replicationStrategy: ReplicationStrategy): Unit = {
    val scripts = FileLoader.entriesAt(settings.migrationScriptPath)
    val registry = parseScripts(scripts)
    val migrator = Migrator(registry, LoggerReporter, settings.migrationTable)
    migrator.initialize(session, settings.keyspace, replicationStrategy)
  }

  /**
    * Connects to cassandra and creates a keyspace
    * @param connConfig connection settings for cassandra
    */
  def initKeyspace(connConfig: CassandraConfig, replicationStrategy: ReplicationStrategy): Unit = {
    val client = new SimpleSyncClient(connConfig)
    client.withSession(initKeyspace(_, replicationStrategy))
  }

  private def parseScripts(scripts: Seq[FileLoader.Entry]): Registry = {
    val parser = Parser()
    val migrations = for {
      script <- scripts
    } yield {
      parser.parse(new ByteArrayInputStream(script.data.getBytes(StandardCharsets.UTF_8)))
    }
    Registry(migrations)
  }
}
