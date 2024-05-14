create table especialidade_paciente
(
    id               serial primary key,
    especialidade_id int,
    paciente_id      int references paciente
);

create table especialidade_psicologo
(
    id               serial primary key,
    especialidade_id int,
    psicologo_id     int references psicologo
);
