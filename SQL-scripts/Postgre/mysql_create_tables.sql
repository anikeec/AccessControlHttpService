\c accesscontroldb

DROP TABLE event_zone_state CASCADE;
DROP TABLE zone_level_state CASCADE;
DROP TABLE fullstatepacket_zonelevelstate CASCADE;
DROP TABLE zone_alarm_state CASCADE;
DROP TABLE fullstatepacket_zonealarmstate CASCADE;
DROP TABLE full_state_packet CASCADE;

CREATE TABLE event_zone_state (
	ezs_id serial NOT NULL,
	zone_number INT,
	zone_state INT,
	zone_counter INT,
	PRIMARY KEY (ezs_id)
);

CREATE TABLE zone_level_state (
	zls_id serial NOT NULL,
	level_state INT,
	PRIMARY KEY (zls_id)
);

CREATE TABLE fullstatepacket_zonelevelstate (
	fspzls_id serial NOT NULL,
	zls_id INT,
	fsp_id INT,
	PRIMARY KEY (fspzls_id)
);

CREATE TABLE zone_alarm_state (
	zas_id serial NOT NULL,
	alarm_state INT,
	PRIMARY KEY (zas_id)
);

CREATE TABLE fullstatepacket_zonealarmstate (
	fspzas_id serial NOT NULL,
	zas_id INT,
	fsp_id INT,
	PRIMARY KEY (fspzas_id)
);

CREATE TABLE full_state_packet(
	fsp_id serial NOT NULL,
	serial_number INT,
	log_event INT,
	date TIMESTAMP,
	time TIMESTAMP,
	ezs_id INT,
	cme_error_number INT,	
	device_temperature VARCHAR(20),
	sygnal_level VARCHAR(20),
	device_voltage VARCHAR(20),	
	packet_number INT,
	active BOOLEAN NOT NULL DEFAULT FALSE,
	PRIMARY KEY (fsp_id)
);

ALTER TABLE full_state_packet ADD CONSTRAINT full_state_packet_fk0 FOREIGN KEY (ezs_id) REFERENCES event_zone_state(ezs_id);

ALTER TABLE fullstatepacket_zonelevelstate ADD CONSTRAINT fullstatepacket_zonelevelstate_fk0 FOREIGN KEY (zls_id) REFERENCES zone_level_state(zls_id);
ALTER TABLE fullstatepacket_zonelevelstate ADD CONSTRAINT fullstatepacket_zonelevelstate_fk1 FOREIGN KEY (fsp_id) REFERENCES full_state_packet(fsp_id);

ALTER TABLE fullstatepacket_zonealarmstate ADD CONSTRAINT fullstatepacket_zonealarmstate_fk0 FOREIGN KEY (zas_id) REFERENCES zone_alarm_state(zas_id);
ALTER TABLE fullstatepacket_zonealarmstate ADD CONSTRAINT fullstatepacket_zonealarmstate_fk1 FOREIGN KEY (fsp_id) REFERENCES full_state_packet(fsp_id);





