
create table dbo.especialidade_paciente
(
    id               int identity
        primary key,
    especialidade_id int,
    paciente_id      int
        constraint FKpaciente_especialidade_paciente
            references dbo.psicologo
)
go

create table dbo.especialidade_psicologo
(
    id               int identity
        primary key,
    especialidade_id int,
    psicologo_id     int
        constraint FKpsicologo_especialidade_psicologo
            references dbo.psicologo
)
go

