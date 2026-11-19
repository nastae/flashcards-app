import { AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms";

export const differentLanguageValidator: ValidatorFn = (
    control: AbstractControl
): ValidationErrors | null => {
    const nativeLanguage = control.get('nativeLanguageCode')?.value;
    const learningLanguage = control.get('learningLanguageCode')?.value;

    if (nativeLanguage &&
        learningLanguage &&
        nativeLanguage === learningLanguage
    ) {
        return { sameLanguages: true }
    }

    return null;
}
