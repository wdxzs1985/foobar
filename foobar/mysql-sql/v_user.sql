CREATE 
VIEW `v_user` AS
    select 
        `u`.`id` AS `id`,
        `u`.`nickname` AS `nickname`,
        `u`.`signature` AS `signature`,
        0 AS `musicCount`,
        0 AS `comicCount`,
        0 AS `followCount`,
        0 AS `followerCount`
    from
        `m_user` `u`
    where
        (`u`.`del_flg` = '0')