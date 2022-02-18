DROP TABLE IF EXISTS appointments;
CREATE TABLE appointments (
                                 user_id BIGINT,
                                 appointment_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                 dose_1_center INT,
                                 dose_2_center INT,
                                 dose_1_status INT,
                                 dose_2_status INT,
                                 dose_1_date DATE,
                                 dose_2_date DATE,
                                 dose_1_brand INT,
                                 dose_2_brand INT
);

INSERT INTO appointments (
                          user_id,
                          appointment_id,
                          dose_1_center,
                          dose_2_center,
                          dose_1_status,
                          dose_2_status,
                          dose_1_date,
                          dose_2_date,
                          dose_1_brand,
                          dose_2_brand
                          ) VALUES(1, 2, 0, null, 0, null, '2022-03-01', null, 0, null);