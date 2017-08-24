package com.evolutiongaming.cassam

import java.util.Date

import com.datastax.driver.core.Session
import com.typesafe.scalalogging.StrictLogging
import de.kaufhof.pillar.{Migration, ReplicationStrategy, Reporter}


object LoggerReporter extends Reporter with StrictLogging {
  def applying(migration: Migration): Unit = {
    logger.info(s"Applying ${migration.key}")
  }
  def initializing(session: Session, keyspace: String): Unit = {
    logger.info(s"Initializing keyspace $keyspace")
  }
  def destroying(session: Session, keyspace: String): Unit = {
    logger.info(s"Destroying keyspace $keyspace")
  }
  def reversing(migration: Migration): Unit = {
    logger.info(s"Reversing ${migration.key}")
  }
  def migrating(session: Session, dateRestriction: Option[Date]): Unit = {
    logger.info(s"Migrating, date restriction: $dateRestriction")
  }
  override def creatingKeyspace(session: Session, keyspace: String, replicationStrategy: ReplicationStrategy): Unit = {
    logger.info(s"creating a keyspace: $keyspace, replication strategy: $replicationStrategy")
  }
  override def creatingMigrationsTable(session: Session, keyspace: String, appliedMigrationsTableName: String): Unit = {
    logger.info(s"creating a migration table[keyspace:$keyspace] - $appliedMigrationsTableName")
  }
}
