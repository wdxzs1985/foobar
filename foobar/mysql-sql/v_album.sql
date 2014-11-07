CREATE
VIEW `v_album` AS
    select 
        `m_article`.`id` AS `id`,
        `m_article`.`title` AS `title`,
        `m_article`.`description` AS `description`,
        `m_article`.`publish_flg` AS `publishFlg`,
        `m_user`.`id` AS `userBean.id`,
        `m_user`.`nickname` AS `userBean.nickname`
    from
        ((`m_album`
        left join `m_article` ON ((`m_album`.`id` = `m_article`.`id`)))
        left join `m_user` ON ((`m_article`.`user_id` = `m_user`.`id`)))
    where
        (`m_article`.`del_flg` = '0')