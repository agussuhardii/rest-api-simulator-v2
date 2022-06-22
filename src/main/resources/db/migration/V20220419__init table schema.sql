create table rest
(
    id                              uuid primary key,
    name                            varchar(50)  not null,
    uri                             varchar(500) not null,
    method                          varchar(20)  not null,
    request_headers                 jsonb        not null,
    request_body                    jsonb        not null,
    request_params                  jsonb        not null,

    success_response_headers        jsonb        not null,
    success_response_body           jsonb        not null,
    success_response_code           varchar(100) not null,

    is_random_success_response_body boolean      not null default false,

    fail_response_headers           jsonb        not null,
    fail_response_body              jsonb        not null,
    fail_response_code              varchar(100) not null,

    response_in_nano_second         int     not null,

    created_at                      numeric,
    updated_at                      numeric,
    unique (uri, method)
);

create table logs
(
    id            uuid
        primary key,
    uri           varchar(500) not null,
    method        varchar(20)  not null,
    headers       jsonb        not null,
    params        jsonb        not null,
    body          jsonb        not null,
    response_code smallint     not null,
    created_at    numeric,
    updated_at    numeric
);
