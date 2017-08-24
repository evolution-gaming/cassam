-- description: create test table1
-- authoredAt: 1
-- up:

CREATE TABLE test_table1 (id text, data text, updated_at timestamp, PRIMARY KEY (id))

-- down:

DROP TABLE test_table1
