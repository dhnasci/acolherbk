alter table especialidade_paciente
drop constraint FKpaciente_especialidade_paciente
go

alter table especialidade_paciente
    add constraint FKespecialidadePaciente_Paciente
        foreign key (paciente_id) references paciente
    go


