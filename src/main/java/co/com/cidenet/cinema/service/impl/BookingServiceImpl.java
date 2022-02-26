package co.com.cidenet.cinema.service.impl;

import co.com.cidenet.cinema.domain.Booking;
import co.com.cidenet.cinema.domain.FunctionFilm;
import co.com.cidenet.cinema.domain.User;
import co.com.cidenet.cinema.repository.BookingRepository;
import co.com.cidenet.cinema.repository.UserRepository;
import co.com.cidenet.cinema.service.BookingService;
import co.com.cidenet.cinema.service.dto.BookingDTO;
import co.com.cidenet.cinema.service.dto.FunctionFilmWithChairDTO;
import co.com.cidenet.cinema.service.mapper.BookingMapper;
import co.com.cidenet.cinema.service.mapper.UtilMapperBooking;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Booking}.
 */
@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    private final UserRepository userRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, BookingMapper bookingMapper, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
        this.userRepository = userRepository;
    }

    @Override
    public BookingDTO save(BookingDTO bookingDTO) {
        log.debug("Request to save Booking : {}", bookingDTO);
        Booking booking = bookingMapper.toEntity(bookingDTO);
        booking = bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }

    @Override
    public Optional<BookingDTO> partialUpdate(BookingDTO bookingDTO) {
        log.debug("Request to partially update Booking : {}", bookingDTO);

        return bookingRepository
            .findById(bookingDTO.getId())
            .map(existingBooking -> {
                bookingMapper.partialUpdate(existingBooking, bookingDTO);

                return existingBooking;
            })
            .map(bookingRepository::save)
            .map(bookingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Bookings");
        return bookingRepository.findAll(pageable).map(bookingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookingDTO> findOne(Long id) {
        log.debug("Request to get Booking : {}", id);
        return bookingRepository.findById(id).map(bookingMapper::toDto);
    }

    @Override
    public void delete(List<Long> id) {
        log.debug("Request to delete Booking : {}", id);
        for (Long charId : id) {
            Booking booking = bookingRepository.getById(charId);
            booking.setUser(null);
            booking.setStatus("Available");
            bookingRepository.save(booking);
        }
    }

    @Override
    public List<Booking> bookingByFunction(Long id) {
        return bookingRepository.findByFunctionFilmId(id);
    }

    @Override
    public List<Booking> bookingChairConfirm(List<BookingDTO> chairSelected) {
        User user = userRepository.findOneByLogin(chairSelected.get(0).getLogin()).get();

        chairSelected.forEach(data -> {
            data.setStatus("Reserved");
            data.setUser(user.getId());
        });

        UtilMapperBooking bookingMapper = new UtilMapperBooking();
        List<Booking> booking = bookingMapper.convertListToEntity(chairSelected);
        return bookingRepository.saveAll(booking);
    }

    @Override
    public List<FunctionFilmWithChairDTO> bookingByUserPage(String user) {
        User userData = userRepository.findOneByLogin(user).get();
        List<Booking> bookingByUser = bookingRepository.findByUser(userData.getId());
        List<FunctionFilmWithChairDTO> functionFilmWithChairDTO = new ArrayList<>();
        // List<FunctionFilmWithChairDTO> aux = new  ArrayList<>();
        FunctionFilm functionFilmData = new FunctionFilm();
        System.out.println(
            "#############################################################################################XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX INICIO DEL PRIMERO"
        );
        for (Booking bookingByUserData : bookingByUser) {
            FunctionFilmWithChairDTO functionFilmWithChair = new FunctionFilmWithChairDTO();

            functionFilmData = bookingByUserData.getFunctionFilm();
            List<Booking> bookingByFunctionUser = new ArrayList<>();
            for (Booking bookingByUserChairs : bookingByUser) {
                if (bookingByUserData.getFunctionFilm().getId() == bookingByUserChairs.getFunctionFilm().getId()) {
                    bookingByFunctionUser.add(bookingByUserChairs);
                }
            }
            int contadorExiste = 0;
            System.out.println(
                "#############################################################################################XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX TAMAÑO DE LA LISTA: " +
                functionFilmWithChairDTO.size()
            );
            for (FunctionFilmWithChairDTO functionFilmWithChairDTOValidator : functionFilmWithChairDTO) {
                System.out.println(
                    "#############################################################################################XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX VALOR DEL CONTADOR: " +
                    contadorExiste
                );
                System.out.println(
                    "#############################################################################################XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX VALOR ID: " +
                    functionFilmData.getId() +
                    "------------" +
                    functionFilmWithChairDTOValidator.getFunctionFilmData().getId()
                );
                if (functionFilmData.getId() == functionFilmWithChairDTOValidator.getFunctionFilmData().getId()) {
                    contadorExiste++;
                    System.out.println(
                        "#############################################################################################XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX ENTRO A LA CONDICIONAL ESTADO CONTADOR: " +
                        contadorExiste
                    );
                }
            }

            System.out.println(
                "#############################################################################################XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX VALOR FINAL CONTADOR: " +
                contadorExiste
            );
            if (contadorExiste < 1) {
                System.out.println(
                    "#############################################################################################XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX ENTRO AL ULTIMO CONDICIONAL: " +
                    contadorExiste
                );
                functionFilmWithChair.setFunctionFilmData(functionFilmData);
                functionFilmWithChair.setChairList(bookingByFunctionUser);
                functionFilmWithChairDTO.add(functionFilmWithChair);
            }
        }

        System.out.println(
            "#############################################################################################XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX FIN DE LOS FOR: "
        );

        // aux = functionFilmWithChairDTO;
        // System.out.println("#############################################################################################XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"+aux.size());
        // for (int i = 0; i <= aux.size(); i++ ) {
        //     FunctionFilmWithChairDTO functionFilmDTOAux = aux.get(i);
        //     int contadorAuxiliar = 0;
        //     for (FunctionFilmWithChairDTO functionFilmWithChairsOriginal : functionFilmWithChairDTO) {
        //         if ( functionFilmDTOAux.getFunctionFilmData().getId() == functionFilmWithChairsOriginal.getFunctionFilmData().getId()){
        //             contadorAuxiliar++;
        //         }
        //     }
        //     if(contadorAuxiliar >= 1 ){
        //         aux.remove(i);
        //     }
        // }

        return functionFilmWithChairDTO;
    }

    @Override
    public void deleteAll(Long id) {
        log.debug("Request to delete All Booking : {}", id);
        Booking booking = bookingRepository.getById(id);
        List<Booking> bookingList = bookingRepository.findByFunctionFilmIdAndUser(booking.getFunctionFilm().getId(), booking.getUser());
        bookingList.forEach(data -> {
            data.setUser(null);
            data.setStatus("Available");
        });
        bookingRepository.saveAll(bookingList);
    }
}
