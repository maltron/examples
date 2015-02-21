//
//  Person.m
//  TestBackgroundSendData
//
//  Created by Mauricio Leal on 2/9/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "Person.h"


@implementation Person

@dynamic firstName;
@dynamic lastName;
@dynamic iD;

-(NSData *)json {
    NSMutableDictionary *dict = [[NSMutableDictionary alloc] init];
    if([self iD]) [dict setObject:[self iD] forKey:@"ID"];
    if([self firstName]) [dict setObject:[self firstName] forKey:@"firstName"];
    if([self lastName]) [dict setObject:[self lastName] forKey:@"lastName"];
    
    return [NSJSONSerialization dataWithJSONObject:dict
                                           options:kNilOptions error:nil];
}

-(void)parseJSON:(NSData *)data {
    NSError *error = nil;
    id json = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:&error];
    
    if(error) {
        NSLog(@"### [Person parseJSON] Error:%@, %@", error, [error userInfo]);
        
    } else if([json isKindOfClass:[NSDictionary class]]) {
        NSLog(@">>> [Person is Dictionary]");
        [self parseJSONfromDictionary:(NSDictionary *)json];
        
    } else if([json isKindOfClass:[NSArray class]]) {
        NSLog(@">>> [Person is Array]");
        NSArray *array = (NSArray *)json;
        
    }
}

-(void)parseJSONfromDictionary:(NSDictionary *)dict {
    id value = [dict objectForKey:@"ID"];
    if(value) [self setID:value]; else [self setID:nil];
    
    value = [dict objectForKey:@"firstName"];
    if(value) [self setFirstName:value]; else [self setFirstName:nil];
    
    value = [dict objectForKey:@"lastName"];
    if(value) [self setLastName:value]; else [self setLastName:nil];
}

-(NSString *)toXML {
    NSString *xml = @"<Person";
    if([self iD]) xml = [xml stringByAppendingString:
                         [NSString stringWithFormat:@" ID=\"%@\">",[self iD]]];
    else xml = [xml stringByAppendingString:@">"];
    
    if([self firstName])
        xml = [NSString stringWithFormat:@"%@<firstName>%@</firstName>", xml, [self firstName]];
    
    if([self lastName])
        xml = [NSString stringWithFormat:@"%@<lastName>%@</lastName>", xml, [self lastName]];
    
    xml = [xml stringByAppendingString:@"</Person>"];
    
    return xml;
}

@end
