package com.grupo1.cursosvulcano.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grupo1.cursosvulcano.dto.request.ReviewRequest;
import com.grupo1.cursosvulcano.dto.response.ReviewResponse;
import com.grupo1.cursosvulcano.model.entity.Module;
import com.grupo1.cursosvulcano.model.entity.Review;
import com.grupo1.cursosvulcano.model.entity.User;
import com.grupo1.cursosvulcano.repository.ModuleRepository;
import com.grupo1.cursosvulcano.repository.ReviewRepository;
import com.grupo1.cursosvulcano.repository.UserRepository;


@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    public List<ReviewResponse> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public ReviewResponse getReviewById(Long id) {
        return reviewRepository.findById(id)
                .map(this::convertToResponse)
                .orElse(null);
    }

    public ReviewResponse saveReview(ReviewRequest reviewRequest) {
        Review review = new Review();
        review.setComment(reviewRequest.getComment());
        review.setRating(reviewRequest.getRating());
        review.setExperience(reviewRequest.getExperience());
        review.setUser(resolveUser(reviewRequest.getUserId()));
        review.setModule(resolveModule(reviewRequest.getModuleId()));
        
        Review savedReview = reviewRepository.save(review);
        return convertToResponse(savedReview);
    }

    public ReviewResponse updateReview(Long id, ReviewRequest reviewRequest) {
        Review existingReview = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review no encontrada"));

        existingReview.setComment(reviewRequest.getComment());
        existingReview.setRating(reviewRequest.getRating());
        existingReview.setExperience(reviewRequest.getExperience());
        existingReview.setUser(resolveUser(reviewRequest.getUserId()));
        existingReview.setModule(resolveModule(reviewRequest.getModuleId()));

        Review updatedReview = reviewRepository.save(existingReview);
        return convertToResponse(updatedReview);
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    private User resolveUser(Long userId) {
        if (userId == null) {
            throw new RuntimeException("El usuario es obligatorio para la review");
        }

        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));
    }

    private Module resolveModule(Long moduleId) {
        if (moduleId == null) {
            throw new RuntimeException("El módulo es obligatorio para la review");
        }

        return moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado con ID: " + moduleId));
    }

    private ReviewResponse convertToResponse(Review review) {
        ReviewResponse response = new ReviewResponse();
        response.setId(review.getId());
        response.setComment(review.getComment());
        response.setRating(review.getRating());
        response.setExperience(review.getExperience());
        response.setUserId(review.getUser().getId());
        response.setModuleId(review.getModule().getId());
        return response;
    }
}
