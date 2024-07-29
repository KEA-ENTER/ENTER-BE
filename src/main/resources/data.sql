INSERT INTO member (
    id,
    employee_no,
    name,
    email,
    password,
    license_id,
    license_password,
    is_license_valid,
    is_agree_terms,
    score,
    role,
    state,
    created_at,
    updated_at
) VALUES (
             2,
             '123456',
             'John Doe',
             'john.doe@example.com',
             'password123',
             'LIC123',
             'licpassword',
             TRUE,
             TRUE,
             100,
             'USER', -- Assuming 'USER' is a valid value for MemberRole
             'ACTIVE', -- Assuming 'ACTIVE' is a valid value for MemberState
             '2023-01-01 00:00:00',
             '2023-01-01 00:00:00'
         );
