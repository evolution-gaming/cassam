package com.evolutiongaming.cassam

import java.time.Instant

import com.evolutiongaming.pillar.Session
import com.typesafe.scalalogging.StrictLogging
import com.evolutiongaming.pillar.{Migration, ReplicationStrategy, Reporter}


object LoggerReporter extends Reporter with StrictLogging {
  override def applying(migration: Migration): Unit = {
    logger.info(s"Applying ${migration.key}")
  }
  def initializing(session: Session, keyspace: String): Unit = {
    logger.info(s"Initializing keyspace $keyspace")
  }
  def destroying(session: Session, keyspace: String): Unit = {
    logger.info(s"Destroying keyspace $keyspace")
  }
  override def reversing(migration: Migration): Unit = {
    logger.info(s"Reversing ${migration.key}")
  }
  override def migrating(session: Session, dateRestriction: Option[Instant]): Unit = {
    logger.info(s"Migrating, date restriction: $dateRestriction")
  }
  override def creatingKeyspace(session: Session, keyspace: String, replicationStrategy: ReplicationStrategy): Unit = {
    logger.info(s"creating a keyspace: $keyspace, replication strategy: $replicationStrategy")
  }
  override def creatingMigrationsTable(session: Session, keyspace: String, appliedMigrationsTableName: String): Unit = {
    logger.info(s"creating a migration table[keyspace:$keyspace] - $appliedMigrationsTableName")
  }
}
