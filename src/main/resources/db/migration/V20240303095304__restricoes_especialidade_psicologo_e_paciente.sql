alter table especialidade_paciente
    add constraint FKespecialidadePacienteEspecialidade
        foreign key (especialidade_id) references especialidade;

alter table especialidade_psicologo
    add constraint FKespecialidadePsicologoEspecialidade
        foreign key (especialidade_id) references especialidade;
