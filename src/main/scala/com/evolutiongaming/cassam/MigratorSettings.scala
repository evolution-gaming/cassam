package com.evolutiongaming.cassam

/**
  * Settings for migrator
  * @param keyspace the target keyspace for migrations
  * @param migrationScriptPath the path to a migration folder
  * @param migrationTable the name of a table which pillar will use to track applied migrations
  */
case class MigratorSettings(
  keyspace: String,
  migrationScriptPath: String,
  migrationTable: String
)
