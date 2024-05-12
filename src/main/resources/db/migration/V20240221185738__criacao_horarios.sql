create table horario
(
    id    serial primary key,
    dia   int,
    turno int
);

create table horariopaciente
(
    id          serial primary key,
    horarioid  int references horario,
    pacienteid int references paciente
);

create table horariopsicologo
(
    id           serial primary key,
    horarioid   int references horario,
    psicologoid int references psicologo
);
