package com.web.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.sql.Date;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@FieldNameConstants
public class PaymentEntity {

    @Column(name = "payment_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    private Float amount;

    @Column(name = "payment_date")
    private Date paymentDate;

    @Column(name = "method")
    private String method;

    @Column(name = "status")
    private String status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_package_id")
    private MemberPackageEntity memberPackage;

    @Size(max = 50)
    @Column(name = "invoice_number", length = 50)
    private String invoiceNumber;

    @Lob
    @Column(name = "notes")
    private String notes;

}
