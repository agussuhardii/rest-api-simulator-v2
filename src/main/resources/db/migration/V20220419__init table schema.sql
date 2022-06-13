create table rest
(
    id                     uuid primary key,
    name                   varchar(50)  not null,
    main_path_url          varchar(50)  not null,
    path_url               varchar(255) not null,
    method                 varchar(20)  not null,
    request_body_or_params jsonb        not null,


    success_header         jsonb        not null,
    success_body           jsonb        not null,

    fail_header            jsonb        not null,
    fail_body              jsonb        not null,

    created_at             numeric,
    updated_at             numeric,
    unique (main_path_url, path_url, method)
);