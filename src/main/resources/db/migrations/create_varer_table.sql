create table varer.vare
(
    ean            bigint  not null
        constraint vare_pk
            primary key,
    navn           varchar not null,
    pris           integer not null,
    plu            smallint,
    beskrivelse    varchar,
    sortimentskode varchar not null,
    kategori       varchar not null
);

comment on table varer.vare is 'Tabell for alle varer som er til salg i butikken';

comment on column varer.vare.ean is 'European Article Number (EAN) er artikkelnummeret for den aktuelle varen.';

comment on column varer.vare.navn is 'Navn på varen.';

comment on column varer.vare.pris is 'Pris på varen.';

comment on column varer.vare.plu is 'Price look-up (PLU) for varer. Eks. er varer uten ean-kode.';

comment on column varer.vare.beskrivelse is 'Eventuell beskrivelse av varen.';

comment on column varer.vare.sortimentskode is 'Sortimentskode for varen.';

comment on column varer.vare.kategori is 'Kategoritype for varen.';

alter table varer.vare
    owner to h577870;

create unique index vare_ean_uindex
    on varer.vare (ean);

create unique index vare_plu_uindex
    on varer.vare (plu);


