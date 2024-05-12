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
    iswhatsapp1  boolean,
    iswhatsapp2  boolean,
    login         varchar(255),
    nomecompleto varchar(255),
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
    iswhatsapp1     boolean,
    iswhatsapp2     boolean,
    jafezterapia   boolean,
    nomecompleto    varchar(255) unique,
    nomeindicacao   varchar(255),
    queixa           varchar(255),
    renda            float,
    escolaridadeid  int,
    generoid        int,
    profissaoid     int,
    registrogeral   varchar(20)
);

create table sessao
(
    id                   serial primary key,
    diaagendado         timestamp,
    feedback             varchar(255),
    iscancelado         boolean,
    ispacienteatendido boolean,
    loginpsicologo      varchar(255),
    motivocancelamento  varchar(255),
    numerosessao        int,
    pacienteid          int references paciente,
    psicologoid         int references psicologo
);

create table triagem
(
    id                  serial primary key,
    diaalocacao        timestamp,
    iscancelado        boolean,
    ispacientealocado boolean,
    loginalocador      varchar(255),
    pacienteid         int references paciente,
    psicologoid        int references psicologo
);
