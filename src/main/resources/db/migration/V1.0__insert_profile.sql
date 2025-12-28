INSERT INTO PROFILE(id,firstname, lastname, patronymic, birthdate, passport_series, passport_number, pinfl, password, gender, visible, status)
VALUES ('c7344901-ceb5-4f33-a7cf-256f17dfb3bb','ruzibek', 'xolmuminov', 'anvarovich', '2008-09-02', 'AD', '1234567','1234567891011121','12345','MALE' ,true,'ACTIVE');
INSERT INTO PROFILE_ROLE(profile_id, roles)
VALUES ('c7344901-ceb5-4f33-a7cf-256f17dfb3bb', 'ROLE_MODERATOR');