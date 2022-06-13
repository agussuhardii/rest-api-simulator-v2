create table rest
(
    id                              uuid primary key,
    name                            varchar(50)  not null,
    path_url                        varchar(255) not null,
    method                          varchar(20)  not null,
    request_body                    jsonb        not null,
    request_params                  jsonb        not null,

    success_response_header         jsonb        not null,
    success_response_body           jsonb        not null,
    is_random_success_response_body boolean      not null default false,

    fail_response_header            jsonb        not null,
    fail_response_body              jsonb        not null,

    created_at                      numeric,
    updated_at                      numeric,
    unique (path_url, method)
);