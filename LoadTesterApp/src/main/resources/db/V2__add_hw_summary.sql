

ALTER TABLE test_run
    ADD COLUMN hw_avg_cpu       double precision,
    ADD COLUMN hw_p95_cpu       double precision,
    ADD COLUMN hw_max_cpu       double precision,
    ADD COLUMN hw_avg_heap_mb   double precision,
    ADD COLUMN hw_max_heap_mb   double precision,
    ADD COLUMN hw_avg_ram_mb    double precision,
    ADD COLUMN hw_max_ram_mb    double precision;

--
-- ALTER TABLE test_run
--     ALTER COLUMN error_message TYPE TEXT;

-- zatim varchar(2048)
