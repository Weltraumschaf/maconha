package de.weltraumschaf.maconha.service.mediafile;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link StringManipulator}.
 */
public final class StringManipulatorTest {
    private final StringManipulator sut = new StringManipulator();

    @Test
    public void removeFileExtension() {
        assertThat(sut.removeFileExtension(null), is(""));
        assertThat(sut.removeFileExtension(""), is(""));
        assertThat(sut.removeFileExtension("  \t  "), is(""));
        assertThat(sut.removeFileExtension(".foo"), is(""));
        assertThat(sut.removeFileExtension("foo"), is("foo"));
        assertThat(sut.removeFileExtension("foo.bar"), is("foo"));
        assertThat(sut.removeFileExtension("foo.bar.baz"), is("foo.bar"));
    }

    @Test
    public void replaceSpecialCharacters() {
        assertThat(sut.replaceSpecialCharacters(null), is(""));
        assertThat(sut.replaceSpecialCharacters(""), is(""));
        assertThat(sut.replaceSpecialCharacters("  \t  "), is(""));
        assertThat(sut.replaceSpecialCharacters("foo"), is("foo"));
        assertThat(sut.replaceSpecialCharacters("foo/bar\\baz-foo_bar.baz"), is("foo bar baz foo bar baz"));
        assertThat(sut.replaceSpecialCharacters("foo--bar___baz"), is("foo  bar   baz"));
    }

    @Test
    public void replaceMultipleWhitespacesWithOne() {
        assertThat(sut.replaceMultipleWhitespacesWithOne(null), is(""));
        assertThat(sut.replaceMultipleWhitespacesWithOne(""), is(""));
        assertThat(sut.replaceMultipleWhitespacesWithOne("  \t  "), is(""));
        assertThat(sut.replaceMultipleWhitespacesWithOne("foo"), is("foo"));
        assertThat(sut.replaceMultipleWhitespacesWithOne("foo bar baz"), is("foo bar baz"));
        assertThat(sut.replaceMultipleWhitespacesWithOne("foo   bar \t baz"), is("foo bar baz"));
    }

    @Test
    public void splitCamelCase() {
        assertThat(sut.splitCamelCase(null), is(""));
        assertThat(sut.splitCamelCase(""), is(""));
        assertThat(sut.splitCamelCase("   "), is(""));
        assertThat(sut.splitCamelCase("FooBarBaz"), is("Foo Bar Baz"));
        assertThat(sut.splitCamelCase("foo"), is("foo"));
        assertThat(sut.splitCamelCase("FOO"), is("FOO"));
        assertThat(sut.splitCamelCase("Foo"), is("Foo"));
        assertThat(sut.splitCamelCase("FOOBar"), is("FOO Bar"));
        assertThat(sut.splitCamelCase("Foo42"), is("Foo 42"));
        assertThat(sut.splitCamelCase("Queensrÿche"), is("Queensrÿche"));
        assertThat(sut.splitCamelCase("Señor"), is("Señor"));
        assertThat(sut.splitCamelCase("Begrüßung"), is("Begrüßung"));
    }
}
