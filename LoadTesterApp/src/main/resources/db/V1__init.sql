create TABLE users(
    id bigserial primary key,
    username varchar(50) not null unique,
    password varchar(256) not null,
    role varchar(16) check ( role in ('ADMIN', 'USER')),
    email varchar(256)
) ;

CREATE TABLE test_run (
    id bigserial primary key,

    total_requests integer NOT NULL CHECK (total_requests > 0),
    status varchar(16) not null check (status in ('CREATED','CANCELLED','APPROVED','REJECTED','WAITING','RUNNING','FINISHED','FAILED')),
    concurrency integer NOT NULL CHECK (concurrency >= 1),

    processing_mode varchar(16) not null check ( processing_mode in ('SERIAL','POOL','VIRTUAL')),
    pool_size_or_cap integer,

    effective_url varchar(2048),
    error_message varchar(2048),

    req_url varchar(1024) not null,
    req_method varchar(16) not null check ( req_method in ('GET','POST','PUT','PATCH','DELETE','HEAD','OPTIONS')),
    req_content_type varchar(128),

    req_body text,

    sum_successes integer,
    sum_failures integer,
    sum_success_rate double precision,
    sum_duration_ms bigint,
    sum_throughput_rps double precision,
    sum_avg_resp_ms double precision,
    sum_p95_resp_ms double precision,
    sum_avg_srv_ms double precision,
    sum_p95_srv_ms double precision,
    sum_avg_queue_ms double precision,
    sum_p95_queue_ms double precision,

    delay_ms bigint CHECK (delay_ms IS NULL OR delay_ms >= 0),

    created_at timestamptz not null default now(),
    started_at timestamptz,
    finished_at timestamptz,

    created_by bigint,
    constraint fk_test_run_user foreign key (created_by) references users(id) on DELETE set null
);

create table test_run_req_header (
    test_run_id bigint not null references test_run(id) on delete cascade,
    header_name varchar(255) not null,
    header_value text not null,
    primary key (test_run_id, header_name)
);

