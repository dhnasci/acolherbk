create table horario
(
    id    serial primary key,
    dia   int,
    turno int
);

create table horario_paciente
(
    id          serial primary key,
    horario_id  int references horario,
    paciente_id int references paciente
);

create table horario_psicologo
(
    id           serial primary key,
    horario_id   int references horario,
    psicologo_id int references psicologo
);
