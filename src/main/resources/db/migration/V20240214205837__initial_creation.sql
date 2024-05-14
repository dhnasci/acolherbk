create table escolaridade
(
    id        serial primary key,
    descricao varchar(255)
);

create table psicologo
(
    id            serial primary key,
    crp           varchar(255),
    cadastro      timestamp,
    celular1      varchar(255),
    celular2      varchar(255),
    email         varchar(255),
    is_whatsapp1  boolean,
    is_whatsapp2  boolean,
    login         varchar(255),
    nome_completo varchar(255),
    perfil        int,
    senha         varchar(255)
);

create table especialidade
(
    id        serial primary key,
    descricao varchar(255)
);

create table genero
(
    id        serial primary key,
    descricao varchar(255)
);

create table profissao
(
    id        serial primary key,
    descricao varchar(255)
);

create table paciente
(
    id               serial primary key,
    cadastro         timestamp,
    celular1         varchar(255),
    celular2         varchar(255),
    idade            int,
    is_whatsapp1     boolean,
    is_whatsapp2     boolean,
    ja_fez_terapia   boolean,
    nome_completo    varchar(255) unique,
    nome_indicacao   varchar(255),
    queixa           varchar(255),
    renda            float,
    escolaridade_id  int,
    genero_id        int,
    profissao_id     int,
    registro_geral   varchar(20)
);

create table sessao
(
    id                   serial primary key,
    dia_agendado         timestamp,
    feedback             varchar(255),
    is_cancelado         boolean,
    is_paciente_atendido boolean,
    login_psicologo      varchar(255),
    motivo_cancelamento  varchar(255),
    numero_sessao        int,
    paciente_id          int references paciente,
    psicologo_id         int references psicologo
);

create table triagem
(
    id                  serial primary key,
    dia_alocacao        timestamp,
    is_cancelado        boolean,
    is_paciente_alocado boolean,
    login_alocador      varchar(255),
    paciente_id         int references paciente,
    psicologo_id        int references psicologo
);
