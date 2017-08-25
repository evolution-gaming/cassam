# Cassam [![Build Status](https://travis-ci.org/evolution-gaming/cassam.svg?branch=master)](https://travis-ci.org/evolution-gaming/cassam)[![Coverage Status](https://coveralls.io/repos/github/evolution-gaming/cassam/badge.svg?branch=master)](https://coveralls.io/github/evolution-gaming/cassam?branch=master) [![Coverage Status](https://coveralls.io/repos/evolution-gaming/akka-tools/badge.svg)](https://coveralls.io/r/evolution-gaming/akka-tools) [ ![version](https://api.bintray.com/packages/evolutiongaming/maven/akka-tools/images/download.svg) ](https://bintray.com/evolutiongaming/maven/akka-tools/_latestVersion)

Cassam(for Cassandra Migrator) is a simple library for cassandra migrations based on [pillar](https://github.com/comeara/pillar).

It creates a keyspace(if necessary), loads migration scripts from a resource folder and executes them.

## How to use
**Step #1**

Put pillar scripts somewhere in `resources` folder. For example in `src/test/resources/test-migrations`.

[`src/test/resources/test-migrations/1_create_test_table1.cql`](src/test/resources/test-migrations/1_create_test_table1.cql):
```
-- description: create test table1
-- authoredAt: 1
-- up:

CREATE TABLE test_table1 (id text, data text, updated_at timestamp, PRIMARY KEY (id))

-- down:

DROP TABLE test_table1
```

More information about a script format is [here](https://github.com/comeara/pillar#migration-files)

**Step #2**

Define settings for `CassandraMigrator`:
```
val testSettings = MigratorSettings(
  keyspace = "test_keyspace",
  migrationScriptPath = "/test-migrations",
  migrationTable = "applied_migrations"
)
```

* **keyspace** - the target keyspace for migrations
* **migrationScriptPath** - the path to a folder with migration scripts
* **migrationTable** - the name of a table which pillar will use to track applied migrations 

**Step #3**

Create a target keyspace in cassandra if necessary using `initKeyspace(...)`:
```
new CassandraMigrator(testSettings).initKeyspace(session)
```

**Step #4**

Execute migration scripts using `migrate(...)`:
```
new CassandraMigrator(testSettings).migrate(session)
```

## Tests

Tests can be found here: `CassandraMigratorSpec`
