alter table especialidade_paciente
    add constraint FKespecialidadePaciente_Especialidade
        foreign key (especialidade_id) references especialidade;

alter table especialidade_psicologo
    add constraint FKespecialidadePsicologo_Especialidade
        foreign key (especialidade_id) references especialidade;
