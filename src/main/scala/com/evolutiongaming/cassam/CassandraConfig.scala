package com.evolutiongaming.cassam

/**
  * Settings for cassandra connection
  */
case class CassandraConfig(
  contactPoints: Seq[String],
  port: Int,
  username: String,
  password: String
)
