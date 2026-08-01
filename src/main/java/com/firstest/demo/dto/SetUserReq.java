package com.firstest.demo.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.Currency;
import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.constraints.UniqueElements;
import org.hibernate.validator.constraints.time.DurationMax;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetUserReq {
    /*
     * // =======================
     * // NULL CHECKS
     * // =======================
     * 
     * // Field must not be null
     * 
     * @NotNull(message = "Field cannot be null")
     * 
     * // Field must not be null, empty, or only whitespace (Strings only)
     * 
     * @NotBlank(message = "Field cannot be blank")
     * 
     * // Field must not be null or empty (String, Collection, Map, Array)
     * 
     * @NotEmpty(message = "Field cannot be empty")
     * 
     * // Field must be null
     * 
     * @Null(message = "Field must be null")
     * 
     * // =======================
     * // STRING VALIDATIONS
     * // =======================
     * 
     * // String length
     * 
     * @Size(min = 3, max = 20, message =
     * "Field must contain between 3 and 20 characters")
     * 
     * // Regular expression
     * 
     * @Pattern(regexp = "^[A-Za-z ]+$", message =
     * "Only alphabets and spaces are allowed")
     * 
     * // Email format
     * 
     * @Email(message = "Invalid email format")
     * 
     * // =======================
     * // NUMBER VALIDATIONS
     * // =======================
     * 
     * // Number must be positive
     * 
     * @Positive(message = "Value must be positive")
     * 
     * // Number must be positive or zero
     * 
     * @PositiveOrZero(message = "Value must be positive or zero")
     * 
     * // Number must be negative
     * 
     * @Negative(message = "Value must be negative")
     * 
     * // Number must be negative or zero
     * 
     * @NegativeOrZero(message = "Value must be negative or zero")
     * 
     * // Minimum value
     * 
     * @Min(value = 18, message = "Minimum value allowed is 18")
     * 
     * // Maximum value
     * 
     * @Max(value = 100, message = "Maximum value allowed is 100")
     * 
     * // Decimal minimum
     * 
     * @DecimalMin(value = "0.01", message = "Value must be at least 0.01")
     * 
     * // Decimal maximum
     * 
     * @DecimalMax(value = "9999.99", message = "Value cannot exceed 9999.99")
     * 
     * // Number of digits
     * 
     * @Digits(integer = 5, fraction = 2, message =
     * "Maximum 5 integer digits and 2 decimal places allowed")
     * 
     * // =======================
     * // BOOLEAN VALIDATIONS
     * // =======================
     * 
     * // Must be true
     * 
     * @AssertTrue(message = "This field must be true")
     * 
     * // Must be false
     * 
     * @AssertFalse(message = "This field must be false")
     * 
     * // =======================
     * // DATE & TIME VALIDATIONS
     * // =======================
     * 
     * // Must be in the past
     * 
     * @Past(message = "Date must be in the past")
     * 
     * // Must be today or past
     * 
     * @PastOrPresent(message = "Date cannot be in the future")
     * 
     * // Must be in the future
     * 
     * @Future(message = "Date must be in the future")
     * 
     * // Must be today or future
     * 
     * @FutureOrPresent(message = "Date cannot be in the past")
     * 
     * // =======================
     * // COLLECTION VALIDATIONS
     * // =======================
     * 
     * // Collection size
     * 
     * @Size(min = 1, max = 5, message = "Collection size must be between 1 and 5")
     * 
     * // =======================
     * // NESTED OBJECT VALIDATION
     * // =======================
     * 
     * // Validate nested object
     * 
     * @Valid
     * 
     * // =======================
     * // HIBERNATE VALIDATOR SPECIFIC
     * // =======================
     * 
     * // URL validation
     * 
     * @URL(message = "Invalid URL")
     * 
     * // Credit card number
     * 
     * @CreditCardNumber(message = "Invalid credit card number")
     * 
     * // ISBN
     * 
     * @ISBN(message = "Invalid ISBN number")
     * 
     * // Currency amount
     * 
     * @Currency(message = "Invalid currency")
     * 
     * // Duration
     * 
     * @DurationMin(seconds = 10, message = "Minimum duration is 10 seconds")
     * 
     * @DurationMax(minutes = 30, message = "Maximum duration is 30 minutes")
     * 
     * // Unique elements in collection
     * 
     * @UniqueElements(message = "Collection contains duplicate elements")
     * 
     * // Range
     * 
     * @Range(min = 18, max = 60, message = "Value must be between 18 and 60")
     * 
     * // Length (String only)
     * 
     * @Length(min = 5, max = 15, message =
     * "Length must be between 5 and 15 characters")
     * 
     * // =======================
     * // CASCADING VALIDATION
     * // =======================
     * 
     * // Validate each element in a list
     * List<@NotBlank(message = "Skill cannot be blank") String> skills;
     * 
     * // =======================
     * // EXAMPLES
     * // =======================
     * 
     * @NotBlank(message = "Name is required")
     * 
     * @Size(min = 3, max = 30, message =
     * "Name must be between 3 and 30 characters")
     * private String name;
     * 
     * @NotBlank(message = "Email is required")
     * 
     * @Email(message = "Enter a valid email address")
     * private String email;
     * 
     * @NotNull(message = "Age is required")
     * 
     * @Min(value = 18, message = "Age must be at least 18")
     * 
     * @Max(value = 60, message = "Age cannot exceed 60")
     * private Integer age;
     * 
     * @NotBlank(message = "Password is required")
     * 
     * @Size(min = 8, message = "Password must contain at least 8 characters")
     * private String password;
     * 
     * @Pattern(regexp = "^[6-9]\\d{9}$", message =
     * "Enter a valid 10-digit mobile number")
     * private String mobile;
     * 
     * @Past(message = "Date of birth must be in the past")
     * private LocalDate dob;
     * 
     * @Positive(message = "Salary must be greater than zero")
     * private Double salary;
     * 
     * @NotEmpty(message = "At least one role must be selected")
     * private List<String> roles;
     */

    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    private MultipartFile avatar;
}