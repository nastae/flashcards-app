-- changeset Aurimas:add-native-word
ALTER TABLE flashcards
ADD COLUMN native_word varchar(255) NULL;
