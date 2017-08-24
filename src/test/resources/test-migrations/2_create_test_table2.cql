-- description: create test table2
-- authoredAt: 2
-- up:

CREATE TABLE test_table2 (id text, data text, updated_at timestamp, PRIMARY KEY (id))

-- down:

DROP TABLE test_table1
