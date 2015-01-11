//
//  AutoLayout.m
//  saruman
//
//  Created by Mauricio Leal on 6/21/14.
//  Copyright (c) 2014 Mauricio "Maltron" Leal. All rights reserved.
//

#import "AutoLayout.h"

@implementation AutoLayout

+(NSMutableArray *)fill:(UIView *)component inside:(UIView *)superview {
    
    NSMutableArray *constraints = [[NSMutableArray alloc] init];
    [constraints addObject:[NSLayoutConstraint constraintWithItem:component
                                                        attribute:NSLayoutAttributeWidth relatedBy:NSLayoutRelationEqual
                                                           toItem:superview
                                                        attribute:NSLayoutAttributeWidth multiplier:1.0f constant:0.0f]];
    [constraints addObject:[NSLayoutConstraint constraintWithItem:component
                                                        attribute:NSLayoutAttributeHeight relatedBy:NSLayoutRelationEqual
                                                           toItem:superview
                                                        attribute:NSLayoutAttributeHeight multiplier:1.0f constant:0.0f]];
    [constraints addObject:[NSLayoutConstraint constraintWithItem:component
                                                        attribute:NSLayoutAttributeCenterX relatedBy:NSLayoutRelationEqual
                                                           toItem:superview
                                                        attribute:NSLayoutAttributeCenterX multiplier:1.0f constant:0.0f]];
    [constraints addObject:[NSLayoutConstraint constraintWithItem:component
                                                        attribute:NSLayoutAttributeCenterY relatedBy:NSLayoutRelationEqual
                                                           toItem:superview
                                                        attribute:NSLayoutAttributeCenterY multiplier:1.0f constant:0.0f]];
    
    return constraints;
}

+(NSMutableArray *)left:(UIView *)component inside:(UIView *)superview {
    if(component) [component setTranslatesAutoresizingMaskIntoConstraints:NO];
    
    NSMutableArray *constraints = [[NSMutableArray alloc] init];
    [constraints addObject:[NSLayoutConstraint constraintWithItem:component
                                                        attribute:NSLayoutAttributeCenterY relatedBy:NSLayoutRelationEqual
                                                           toItem:superview
                                                        attribute:NSLayoutAttributeCenterY multiplier:1.0f constant:0.0f]];
    [constraints addObject:[NSLayoutConstraint constraintWithItem:component
                                                        attribute:NSLayoutAttributeTrailing relatedBy:NSLayoutRelationEqual
                                                           toItem:superview
                                                        attribute:NSLayoutAttributeLeading multiplier:1.0f constant:PADDING]];
    
    return constraints;
}

+(NSMutableArray *)distribute:(UIView *)left withMaxWidth:(CGFloat)maxWidth and:(UIView *)right inside:(UIView *)superview {
    if(left) {
        [left setTranslatesAutoresizingMaskIntoConstraints:NO];
        [superview addSubview:left];
    }
    
    if(right) {
        [right setTranslatesAutoresizingMaskIntoConstraints:NO];
        [superview addSubview:right];
    }
    
    NSMutableArray *constraints = [[NSMutableArray alloc] init];
    if(left) {
        // Max Width (if any)
        if(maxWidth > 0)
            [constraints addObject:[NSLayoutConstraint constraintWithItem:left
                                                                attribute:NSLayoutAttributeWidth relatedBy:NSLayoutRelationEqual
                                                                   toItem:nil
                                                                attribute:NSLayoutAttributeNotAnAttribute multiplier:1.0f
                                                                 constant:maxWidth]];
        
        [constraints addObject:[NSLayoutConstraint constraintWithItem:left
                                                            attribute:NSLayoutAttributeCenterY relatedBy:NSLayoutRelationEqual
                                                               toItem:superview
                                                            attribute:NSLayoutAttributeCenterY multiplier:1.0f constant:0.0f]];
        [constraints addObject:[NSLayoutConstraint constraintWithItem:left
                                                            attribute:NSLayoutAttributeLeading relatedBy:NSLayoutRelationEqual
                                                               toItem:superview
                                                            attribute:NSLayoutAttributeLeading multiplier:1.0f constant:PADDING]];
    }
    
    if(right) {
        [constraints addObject:[NSLayoutConstraint constraintWithItem:right
                                                            attribute:NSLayoutAttributeCenterY relatedBy:NSLayoutRelationEqual
                                                               toItem:superview
                                                            attribute:NSLayoutAttributeCenterY multiplier:1.0f constant:0.0f]];
        [constraints addObject:[NSLayoutConstraint constraintWithItem:right
                                                            attribute:NSLayoutAttributeTrailing relatedBy:NSLayoutRelationEqual
                                                               toItem:superview
                                                            attribute:NSLayoutAttributeTrailing multiplier:1.0f constant:-PADDING]];
    }
    
    return constraints;
}

@end
