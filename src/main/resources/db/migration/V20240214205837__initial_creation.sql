create table dbo.escolaridade
(
    id        int identity
        primary key,
    descricao varchar(255)
)
go

create table dbo.psicologo
(
    id            int identity
        primary key,
    crp           varchar(255),
    cadastro      datetime2,
    celular1      varchar(255),
    celular2      varchar(255),
    email         varchar(255),
    is_whatsapp1  bit,
    is_whatsapp2  bit,
    login         varchar(255),
    nome_completo varchar(255),
    perfil        int,
    senha         varchar(255)
)
go

create table especialidade
(
    id           int identity
        primary key,
    descricao    varchar(255),
    psicologo_id int
        constraint FKespecialidade_psicologo
            references dbo.psicologo
)
go

create table dbo.genero
(
    id        int identity
        primary key,
    descricao varchar(255)
)
go

create table dbo.profissao
(
    id        int identity
        primary key,
    descricao varchar(255)
)
go

create table dbo.paciente
(
    id               int identity
        primary key,
    cadastro         datetime2,
    celular1         varchar(255),
    celular2         varchar(255),
    idade            int,
    is_whatsapp1     bit,
    is_whatsapp2     bit,
    ja_fez_terapia   bit,
    nome_completo    varchar(255),
    nome_indicacao   varchar(255),
    queixa           varchar(255),
    renda            float,
    escolaridade_id  int
        constraint FKpaciente_escolaridade
            references dbo.escolaridade,
    especialidade_id int
        constraint FKpaciente_especialidade
            references dbo.especialidade,
    genero_id        int
        constraint FKpaciente_genero
            references dbo.genero,
    profissao_id     int
        constraint FKpaciente_profissao
            references dbo.profissao
)
go



create table dbo.sessao
(
    id                   int identity
        primary key,
    dia_agendado         datetime2,
    feedback             varchar(255),
    is_cancelado         bit,
    is_paciente_atendido bit,
    login_psicologo      varchar(255),
    motivo_cancelamento  varchar(255),
    numero_sessao        int,
    paciente_id          int
        constraint FKsessao_paciente
            references dbo.paciente,
    psicologo_id         int
        constraint FKsessao_psicologo
            references dbo.psicologo
)
go

create table dbo.triagem
(
    id                  int identity
        primary key,
    dia_alocacao        datetime2,
    is_cancelado        bit,
    is_paciente_alocado bit,
    login_alocador      varchar(255),
    paciente_id         int
        constraint FKtriagem_paciente
            references dbo.paciente,
    psicologo_id        int
        constraint FKtriagem_psicologo
            references dbo.psicologo
)
go



