create table users (
    id bigserial primary key,
    first_name varchar(100) not null,
    last_name varchar(100) not null,
    email varchar(100) unique not null,
    password varchar(255) not null,
    created_at timestamp default current_timestamp,
    role varchar(100) not null
);

create table teachers (
    user_id bigint primary key references users(id),
    employment_status int
);

create table students (
    user_id bigint primary key references users(id)
);

create table subjects (
    id bigserial primary key,
    name varchar(255) not null,
    description text,
    difficulty_level int,
    created_at timestamp default current_timestamp,
    teacher_id bigint not null references teachers(user_id)
);

create table student_subject_access (
    student_id bigint not null references students(user_id) on delete cascade,
    subject_id bigint not null references subjects(id) on delete cascade,
    access_granted_at timestamp default current_timestamp,
    primary key(student_id, subject_id)
);


create table tests (
    id bigserial primary key,
    title varchar(255) not null,
    description text,
    start_date timestamp,
    end_date timestamp,
    duration_minutes int,
    max_points int not null,
    created_at timestamp default current_timestamp,
    subject_id bigint not null references subjects(id) on delete cascade
);

create table questions (
    id bigserial primary key,
    question_text text not null,
    question_type int not null,
    max_points float not null,
    test_id bigint not null references tests(id) on delete cascade
);

create table question_images (
    id bigserial primary key,
    image_path varchar(500) not null,
    question_id bigint not null references questions(id) on delete cascade
);


create table answers (
    id bigserial primary key,
    answer_text text not null,
    is_correct boolean not null default false,
    question_id bigint not null references questions(id) on delete cascade
);

create table test_attempts (
    id bigserial primary key,
    status int not null,
    total_score float default 0,
    submission_date timestamp,
    test_id bigint not null references tests(id) on delete cascade,
    student_id bigint not null references students(user_id) on delete set null
);


create table responses (
    id bigserial primary key,
    score float default 0,
    test_attempt_id bigint not null references test_attempts(id) on delete cascade,
    question_id bigint not null references questions(id) on delete cascade
);

create table open_ended_responses (
    response_id bigint primary key references responses(id) on delete cascade,
    answer_text text
);

create table evaluations (
    id bigserial primary key,
    comment text,
    score float default 0,
    is_AI_generated boolean default true,
    open_ended_response_id bigint not null references open_ended_responses(response_id) on delete cascade
);

create table choice_responses (
    response_id bigint primary key references responses(id) on delete cascade
);

create table choice_response_answers(
    id bigserial primary key,
    choice_response_id bigint not null references choice_responses(response_id) on delete cascade,
    answer_id bigint not null references answers(id) on delete cascade
);

