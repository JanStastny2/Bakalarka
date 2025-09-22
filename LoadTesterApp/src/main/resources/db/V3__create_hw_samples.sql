
CREATE TABLE test_run_hw_sample(
    id  bigserial primary key ,
    test_run_id bigint not null references test_run(id) on delete cascade,
    ts timestamptz not null ,
    cpu double precision,
    heap_mb double precision,
    ram_usage double precision
                               );