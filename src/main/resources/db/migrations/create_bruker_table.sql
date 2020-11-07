create table varer.bruker
(
	passord varchar not null,
	brukernavn varchar not null
		constraint bruker_pkey
			primary key,
	privilege boolean default false not null
);

comment on table varer.bruker is 'Tabell for brukerinnlogginger';

comment on column varer.bruker.passord is 'Passord for bruker. Hashet og saltet.';

comment on column varer.bruker.brukernavn is 'Brukernavn for bruker.';

comment on column varer.bruker.privilege is 'Avgjør om brukeren har admin-privilegier';

alter table varer.bruker owner to h577870;

create unique index bruker_passord_uindex
	on varer.bruker (passord);

