create table empresa
(
    id         serial
        primary key,
    nome       varchar(100)                                       not null,
    cnpjcpf    varchar(20)                                        not null
        unique,
    usuario    varchar(12)                                        not null
        unique,
    email      varchar(50)                                        not null
        unique,
    endereco   varchar(255),
    pago       boolean     default true                          not null,
    cadastro   date        default CURRENT_DATE                   not null,
    vencimento integer     default 5                              not null
        constraint empresa_vencimento_check
            check ((vencimento >= 1) AND (vencimento <= 31)),
    token      varchar(36) default '0000-0000'::character varying not null
);

alter table empresa
    owner to postgres;

