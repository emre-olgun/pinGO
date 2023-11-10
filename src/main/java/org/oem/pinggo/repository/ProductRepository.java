package org.oem.pinggo.repository;

import jakarta.validation.constraints.NotNull;
import org.oem.pinggo.entity.Product;
import org.oem.pinggo.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface ProductRepository extends JpaRepository<Product, Long> {
    //  @Query("select p from Product p where p.quantity >= ?1")
    List<Product> findByQuantityGreaterThanEqual(int quantity);


    @Query("select p from Product p where p.seller.id = ?1")
    Stream<Product> findBySeller(Seller seller);

    @Transactional
    @Modifying
    @Query("update Product p set p.seller = ?1 where p.id = ?2")
    int updateSeller(Seller seller, Long id);






    @Query("select p from Product p where (upper(p.name) like upper(:qText) or upper(p.description) like upper(:qText)) and p.quantity>0")
    List<Product> findWithQueryText(@Param("qText") String qText);

    @Override
    Optional<Product> findById(Long aLong);

    @Query("select (count(p) > 0) from Product p where p.id = :pID and p.seller.id = :sID")
    boolean existsProductSellerwithIDs(@Param("pID") Long pID, @Param("sID") Long sID);

    
    
}