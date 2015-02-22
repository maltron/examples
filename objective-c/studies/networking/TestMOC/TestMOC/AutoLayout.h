//
//  AutoLayout.h
//  saruman
//
//  Created by Mauricio Leal on 6/21/14.
//  Copyright (c) 2014 Mauricio "Maltron" Leal. All rights reserved.
//

#import <UIKit/UIKit.h>

#define PADDING 16.0f

@interface AutoLayout : NSObject

+(NSMutableArray *)fill:(UIView *)component inside:(UIView *)superview;
+(NSMutableArray *)distribute:(UIView *)left withMaxWidth:(CGFloat)maxWidth and:(UIView *)right inside:(UIView *)superview;

@end
