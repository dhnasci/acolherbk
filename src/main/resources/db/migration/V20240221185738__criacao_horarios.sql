
create table horario
(
    id    int identity
        primary key,
    dia   int,
    turno int
)
go

create table horario_paciente
(
    id          int identity
        primary key,
    horario_id  int
        constraint FKhorarioPaciente_Horario
            references horario,
    paciente_id int
        constraint FKhorarioPaciente_Paciente
            references paciente
)
go
create table horario_psicologo
(
    id           int identity
        primary key,
    horario_id   int
        constraint FKhorarioPsicologo_Horario
            references horario,
    psicologo_id int
        constraint FKhorarioPsicologo_Psicologo
            references psicologo
)
go

