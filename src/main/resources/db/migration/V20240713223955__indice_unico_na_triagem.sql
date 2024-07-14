alter table triagem
    add constraint UTriagem
        unique (psicologo_id, paciente_id);
