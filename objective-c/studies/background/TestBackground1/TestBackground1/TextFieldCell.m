//
//  TextFieldCell.m
//  TeamPlayerCoreDataExample
//
//  Created by Mauricio Leal on 1/2/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "TextFieldCell.h"

@implementation TextFieldCell
@synthesize label = _label;
@synthesize textField = _textField;
@synthesize maxWidth = _maxWidth;

-(id)initWithLabel:(UILabel *)label andTextField:(UITextField *)textField withMaxWidth:(CGFloat)maxWidth reuseIdentifier:(NSString *)reuseIdentifer {
    self = [super initWithStyle:UITableViewCellStyleDefault reuseIdentifier:reuseIdentifer];
    if(self) {
        _label = label;
        _textField = textField;
        _maxWidth = maxWidth;
    }
    // Disable selection in this cell
    [self setSelectionStyle:UITableViewCellSelectionStyleNone];
    
    NSMutableArray *constraints = [[NSMutableArray alloc] init];
    if(_label) {
        // Enable Auto Layout
        [self.label setTranslatesAutoresizingMaskIntoConstraints:NO];
        [self.contentView addSubview:self.label];
        
        // Center Y
        [constraints addObject:[NSLayoutConstraint constraintWithItem:self.label
                                                            attribute:NSLayoutAttributeCenterY relatedBy:NSLayoutRelationEqual
                                                               toItem:self.contentView
                                                            attribute:NSLayoutAttributeCenterY multiplier:1.0f constant:0.0f]];
        // Max Width
        [constraints addObject:[NSLayoutConstraint constraintWithItem:self.label
                                                            attribute:NSLayoutAttributeWidth relatedBy:NSLayoutRelationEqual
                                                               toItem:nil
                                                            attribute:NSLayoutAttributeNotAnAttribute multiplier:1.0f
                                                             constant:self.maxWidth]];
        // Left Aligned
        [constraints addObject:[NSLayoutConstraint constraintWithItem:self.label
                                                            attribute:NSLayoutAttributeLeading relatedBy:NSLayoutRelationEqual
                                                               toItem:self.contentView
                                                            attribute:NSLayoutAttributeLeading multiplier:1.0f constant:PADDING]];
    }
    
    if(_textField) {
        // Enable Auto Layout
        [self.textField setTranslatesAutoresizingMaskIntoConstraints:NO];
        [self.contentView addSubview:self.textField];
        
        // Center Y
        [constraints addObject:[NSLayoutConstraint constraintWithItem:self.textField
                                                            attribute:NSLayoutAttributeCenterY relatedBy:NSLayoutRelationEqual
                                                               toItem:self.contentView
                                                            attribute:NSLayoutAttributeCenterY multiplier:1.0f constant:0.0f]];
        // Leading
        [constraints addObject:[NSLayoutConstraint constraintWithItem:self.textField
                                                            attribute:NSLayoutAttributeLeading relatedBy:NSLayoutRelationEqual
                                                               toItem:_label != nil ? self.label : self.contentView
                                                            attribute:_label != nil ? NSLayoutAttributeTrailing : NSLayoutAttributeLeading
                                                           multiplier:1.0f constant:PADDING]];
        // Trailing
        [constraints addObject:[NSLayoutConstraint constraintWithItem:self.textField
                                                            attribute:NSLayoutAttributeTrailing relatedBy:NSLayoutRelationEqual
                                                               toItem:self.contentView
                                                            attribute:NSLayoutAttributeTrailing multiplier:1.0f constant:-PADDING]];
    }
    
    [self.contentView addConstraints:constraints];
    
    return self;
}

@end
