create table app_user (
                          id bigserial primary key,
                          first_name varchar(100) not null,
                          last_name varchar(100) not null,
                          email varchar(255) not null unique,
                          birth_date date not null,
                          role varchar(20) not null,
                          password varchar(255) not null
);

create table race (
                      id bigserial primary key,
                      name varchar(150) not null,
                      distance varchar(30) not null
);

create table race_entry (
                            id bigserial primary key,
                            race_id bigint not null references race(id) on delete cascade,
                            user_id bigint not null references app_user(id) on delete cascade,
                            created_at timestamp not null default now(),
                            constraint uq_race_user unique (race_id, user_id)
);