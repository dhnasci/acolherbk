alter table especialidadepaciente
    add constraint FKespecialidadePacienteEspecialidade
        foreign key (especialidadeid) references especialidade;

alter table especialidadepsicologo
    add constraint FKespecialidadePsicologoEspecialidade
        foreign key (especialidadeid) references especialidade;
