create table escolaridade
(
    id        int not null
        constraint escolaridade_pk
            primary key,
    descricao varchar
)
go

create table especialidade
(
    id        int not null
        constraint especialidade_pk
            primary key,
    descricao varchar
)
go

create table genero
(
    id        int
        constraint genero_pk
            primary key,
    descricao varchar
)
go

create table profissao
(
    id        int
        constraint profissao_pk
            primary key,
    descricao varchar
)
go



