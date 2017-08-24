package com.evolutiongaming.cassam

import com.datastax.driver.core.{Cluster, Session}

/**
  * Simple cassandra client
  */
private[cassam] class SimpleSyncClient(settings: CassandraConfig) {
  def withSession[T](func: Session => T): T = {
    val cluster = Cluster
      .builder()
      .addContactPoints(settings.contactPoints:_*)
      .withPort(settings.port)
      .withCredentials(settings.username, settings.password)
      .build()

    val session = cluster.connect()
    func(session)
  }
}