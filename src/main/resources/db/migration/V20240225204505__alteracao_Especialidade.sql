create table especialidadepaciente
(
    id               serial primary key,
    especialidadeid int,
    pacienteid      int references paciente
);

create table especialidadepsicologo
(
    id               serial primary key,
    especialidadeid int,
    psicologoid     int references psicologo
);
