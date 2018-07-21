import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/books")
    public List<Book> getAllBooks(){
        List<Book> books = new ArrayList<Book>();
        Iterable<Book> bookIterable = bookRepository.findAll();
        bookIterable.forEach(books::add);
        return books;
    }

    @PostMapping("/books/create")
    public Book createBook(@Valid @RequestBody Book book) {
        System.out.println("Book create ...");
        return bookRepository.save(book);
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBook(@PathVariable("id") Long id){
        System.out.println("Get book by id ...");
        Optional<Book> bookData = bookRepository.findById(id);
        if(bookData.isPresent()){
            return new ResponseEntity<>(bookData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/books/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable("id") Long id, @RequestBody Book book) {
        System.out.println("UpdateBook");
        Optional<Book> bookData = bookRepository.findById(id);


        if (bookData.isPresent()) {
            Book savedBook = bookData.get();
            savedBook.setAuthor(book.getAuthor());
            savedBook.setDescription(book.getDescription());
            savedBook.setTitle(book.getTitle());
            Book updatedBook = bookRepository.save(savedBook);
            return new ResponseEntity<>(updatedBook, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/books/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable("id") Long id) {
        System.out.println("Delete Book with ID = " + id + "...");

        try {
            bookRepository.deleteById(id);
        } catch (Exception e) {
            return new ResponseEntity<>("Fail to delete!", HttpStatus.EXPECTATION_FAILED);
        }

        return new ResponseEntity<>("Book has been deleted!", HttpStatus.OK);
    }

}
