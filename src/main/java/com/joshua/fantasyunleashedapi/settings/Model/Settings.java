package com.joshua.fantasyunleashedapi.settings.Model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
// settings model
public class Settings{
    @Id
    @Column(name="settings_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer settingsId;
    @Column(name="qb_tackle")
    private Boolean qbTackle;
    @Column(name="kick_return")
    private Boolean kickReturn;
    @Column(name="non_qb_throw_td")
    private Boolean nonQbThrowTd;
    @Column(name="qb_receiving_td")
    private Boolean qbReceivingTd;
    @Column(name="qb_tackle_value")
    private Integer qbTackleValue;
    @Column(name="kick_return_value")
    private Integer kickReturnValue;
    @Column(name="non_qb_throw_td_value")
    private Integer nonQbThrowTdValue;
    @Column(name="qb_receiving_td_value")
    private Integer qbReceivingTdValue;
}
