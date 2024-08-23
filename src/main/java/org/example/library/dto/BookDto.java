    package org.example.library.dto;

    import jakarta.persistence.CascadeType;
    import jakarta.persistence.Column;
    import jakarta.persistence.JoinColumn;
    import jakarta.persistence.ManyToOne;
    import jakarta.validation.Valid;
    import jakarta.validation.constraints.*;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import lombok.ToString;
    import org.example.library.entities.Student;

    import java.time.LocalDate;

    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public class BookDto {

        @NotNull
        @Digits(integer = 4, fraction = 0, message = "Please enter a valid bookId")
        @Column(unique = true)
        @Min(value = 1, message = "min 1 digits and max 4 digit required  for bookId ")
        @Max(value = 9999, message = "bookId must be four digits")
        @Positive(message = "bookId must be greater than zero")
        private int bookId;
        @NotEmpty
        @NotBlank(message = "book name should not be empty")
        private String bookName;

        @NotBlank(message ="book author should not be blank")
        private String bookAuthor;


        @NotNull(message = "Please enter a year")
        @Digits(integer = 4, fraction = 0, message = "Please enter a valid four-digit year")
        @Min(value = 1, message = "Year must be between 1 and 9999")
        @Max(value = 9999, message = "Year must be between 1 and 9999")
        private int bookYear;



        private LocalDate DateOfIssue;

        private LocalDate DateOfSubmission;



        private int totalDaysOfIssueBook;



        @Positive(message = "book semester must be positive number")
        private int bookSemester;

        private String fine;


        private StudentDto studentDto;



    }
