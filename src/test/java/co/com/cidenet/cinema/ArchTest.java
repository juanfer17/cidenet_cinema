package co.com.cidenet.cinema;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("co.com.cidenet.cinema");

        noClasses()
            .that()
            .resideInAnyPackage("co.com.cidenet.cinema.service..")
            .or()
            .resideInAnyPackage("co.com.cidenet.cinema.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..co.com.cidenet.cinema.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
